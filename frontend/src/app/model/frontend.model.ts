export interface Startup {
  id: number;
  nome: string;
  slogan: string;
  anoFundacao: string;
  pontuacao: number;
  eliminada: boolean;
  investidorSecretoUsado: boolean;
}

export interface Batalha {
  id: number;
  rodada: number;
  finalizada: boolean;
  pontuacaoA: number;
  pontuacaoB: number;
  startupA: Startup;
  startupB: Startup;
  resultado: string;
}

export type TipoEvento =
  | 'PITCH_CONVINCENTE'
  | 'PRODUTO_COM_BUGS'
  | 'BOA_TRACAO_USUARIOS'
  | 'INVESTIDOR_IRRITADO'
  | 'FAKE_NEWS';

export interface EventoRequestDTO {
  idStartup: number;
  tipoEvento: TipoEvento;
}

export interface TorneioStatusDTO {
  rodadaAtual: number;
  batalhasFinalizadas: Batalha[];
  batalhasPendentes: Batalha[];
  startupsAtivas: Startup[];
  campea: Startup | null;
}


